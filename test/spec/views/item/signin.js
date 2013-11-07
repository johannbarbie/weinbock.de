(function() {
	'use strict';

	var root = this;

	root.define([
		'views/item/signin'
		],
		function( Signin ) {

			describe('Signin Itemview', function () {

				it('should be an instance of Signin Itemview', function () {
					var signin = new Signin();
					expect( signin ).to.be.an.instanceof( Signin );
				});

				it('should have more test written', function(){
					expect( false ).to.be.ok;
				});
			});

		});

}).call( this );