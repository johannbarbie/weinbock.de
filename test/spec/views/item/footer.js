(function() {
	'use strict';

	var root = this;

	root.define([
		'views/item/footer'
		],
		function( Footer ) {

			describe('Footer Itemview', function () {

				it('should be an instance of Footer Itemview', function () {
					var footer = new Footer();
					expect( footer ).to.be.an.instanceof( Footer );
				});

				it('should have more test written', function(){
					expect( false ).to.be.ok;
				});
			});

		});

}).call( this );