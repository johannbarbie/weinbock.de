define([
	'backbone',
	'hbs!tmpl/item/productView_tmpl'
],
function( Backbone, ProductviewTmpl  ) {
    'use strict';

	/* Return a ItemView class definition */
	return Backbone.Marionette.ItemView.extend({

		initialize: function() {
			console.log("initialize a Productview ItemView");
		},
		className: 'accordion-group',
    	template: ProductviewTmpl,
        

    	/* ui selector cache */
    	ui: {},

		/* Ui events hash */
		events: {},

		onShow: function(){
			$('#collapse'+this.model.get('id')).collapse({"toggle": 'accordion', 'parent': '#myaccordion'});
		},

		/* on render callback */
		onRender: function() {}
	});

});
