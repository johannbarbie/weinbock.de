(function() {
	'use strict';

	var root = this;

	root.define([
		'views/item/marketing'
		],
		function( Marketing ) {

			describe('Marketing Itemview', function () {

				it('should be an instance of Marketing Itemview', function () {
					var marketing = new Marketing();
					expect( marketing ).to.be.an.instanceof( Marketing );
				});

				it('should have more test written', function(){
					expect( false ).to.be.ok;
				});
			});

		});

}).call( this );