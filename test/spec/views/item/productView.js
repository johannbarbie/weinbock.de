(function() {
	'use strict';

	var root = this;

	root.define([
		'views/item/productView'
		],
		function( Productview ) {

			describe('Productview Itemview', function () {

				it('should be an instance of Productview Itemview', function () {
					var productView = new Productview();
					expect( productView ).to.be.an.instanceof( Productview );
				});
			});

		});

}).call( this );