(function() {
	'use strict';

	var root = this;

	root.define([
		'views/composite/productsview'
		],
		function( Productsview ) {

			describe('Productsview Compositeview', function () {

				it('should be an instance of Productsview Compositeview', function () {
					var productsview = new Productsview();
					expect( productsview ).to.be.an.instanceof( Productsview );
				});

			});

		});

}).call( this );