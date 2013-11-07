(function() {
	'use strict';

	var root = this;

	root.define([
		'views/item/space'
		],
		function( Space ) {

			describe('Space Itemview', function () {

				it('should be an instance of Space Itemview', function () {
					var space = new Space();
					expect( space ).to.be.an.instanceof( Space );
				});

				it('should have more test written', function(){
					expect( false ).to.be.ok;
				});
			});

		});

}).call( this );