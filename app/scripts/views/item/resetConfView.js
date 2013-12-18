define([
	'backbone',
	'hbs!tmpl/item/resetConfView_tmpl'
],
function( Backbone, ResetConfTmpl  ) {
    'use strict';

	/* Return a ItemView class definition */
	return Backbone.Marionette.ItemView.extend({

		initialize: function() {
			console.log('initialize a Reset Conf ItemView');
			this.model.on('error', this.onError, this);
		},

		template: ResetConfTmpl,
        

		/* ui selector cache */
		ui: {},

		/* Ui events hash */
		events: {
            'click button.btn-primary':'handleReset',
            'blur input[name="password1"]':'checkPassword1'
        },

        checkPassword1: function(e){
			this.$('button.btn-primary').button('reset');
        },

        handleReset: function(e){
			e.preventDefault();
			var pw1 = this.$('input[name="password1"]').val();
			var pw2 = this.$('input[name="password2"]').val();
			if (pw1 === pw2){
				this.model.set('password',pw1);
				this.model.save();
	        }else{
				this.$('div.alert').show();
	        }
        },

		/* on render callback */
		onRender: function() {
			this.$('.alert').alert();
            this.$('div.alert').hide();
            var self = this;
            console.log(this.model.get('token'));
            $.get( window.opt.basePath + '/account/password/ticket',
            	{ticket: this.model.get('token')},
            	function( data ) {
				if (data.value === 'inactive'){
					self.$('form.form-signin').hide();
					self.$('div.alert').show();
				}
		    });
		}
	});

});
