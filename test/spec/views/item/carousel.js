(function() {
	'use strict';

	var root = this;

	root.define([
		'views/item/carousel'
		],
		function( Carousel ) {

			describe('Carousel Itemview', function () {

				it('should be an instance of Carousel Itemview', function () {
					var carousel = new Carousel();
					expect( carousel ).to.be.an.instanceof( Carousel );
				});

				it('should have more test written', function(){
					expect( false ).to.be.ok;
				});
			});

		});

}).call( this );