define([
	'backbone',
	'hbs!tmpl/item/resetView_tmpl'
],
function( Backbone, ResetTmpl  ) {
    'use strict';

	/* Return a ItemView class definition */
	return Backbone.Marionette.ItemView.extend({

		initialize: function() {
			console.log('initialize a Reset ItemView');
		},

		template: ResetTmpl,
        

		/* ui selector cache */
		ui: {},

		/* Ui events hash */
		events: {
			'click button.btn-primary':'handleReset'
		},

		handleReset: function(e){
			e.preventDefault();
			var user = this.$('input:text').val();
			this.model.set('email',user);
			this.model.save();
		},

		/* on render callback */
		onRender: function() {
			this.$('.alert').alert();
            this.$('div.alert').hide();
		}
	});

});
