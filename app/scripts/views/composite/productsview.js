define([
	'backbone',
	'views/item/productView',
	'hbs!tmpl/composite/productsview_tmpl'
],
function( Backbone, Productview, ProductsviewTmpl  ) {
    'use strict';

	/* Return a CompositeView class definition */
	return Backbone.Marionette.CompositeView.extend({

		initialize: function() {
			console.log("initialize a Productsview CompositeView");
		},
    	itemView: Productview,
    	
    	template: ProductsviewTmpl,
    	

    	/* ui selector cache */
    	ui: {},

    	/* where are we appending the items views */
    	itemViewContainer: "div.accordion",

		/* Ui events hash */
		events: {},

		onShow: function(){
			this.$('#myaccordion').on('show hide', function() {
		        $('div.accordion').css('height', 'auto');
		    });
		},


		/* on render callback */
		onRender: function() {}
	});

});
