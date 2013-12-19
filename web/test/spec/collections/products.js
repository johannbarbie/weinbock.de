(function() {
	'use strict';

	var root = this;

	root.define([
		'collections/products'
		],
		function( Products ) {

			describe('Products Collection', function () {

				it('should be an instance of Products Collection', function () {
					var products = new Products();
					expect( products ).to.be.an.instanceof( Products );
				});
			});

		});

}).call( this );