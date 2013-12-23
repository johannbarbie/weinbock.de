define([
	'backbone',
	'communicator',
	'hbs!tmpl/header'
],
function( Backbone, Communicator, HeaderTmpl  ) {
    'use strict';

	/* Return a ItemView class definition */
	var view = Backbone.Marionette.ItemView.extend({

		initialize: function() {
			console.log('initialize a Header ItemView');
			var vent = Communicator.mediator;
			var self = this;
			vent.on('app:login', function(){
				self.setButton();
			});
		},
		className: 'container',
		template: HeaderTmpl,


		/* ui selector cache */
		ui: {},

		/* Ui events hash */
		events: {
			'click #aLogout':'handleLogout'
		},
		setButton: function(){
			if (sessionStorage.getItem('roles')){
				this.$('#liLogin').hide();
				this.$('#liLogout').show();
			}else{
				this.$('#liLogout').hide();
				this.$('#liLogin').show();
			}
		},
		handleLogout: function(e){
			e.preventDefault();
			this.$('#liLogout').hide();
			this.$('#liLogin').show();
			Communicator.mediator.trigger('app:logout');
		},
		/* on render callback */
		onShow: function() {
			this.setButton();
		}
	});

	return view;

});
