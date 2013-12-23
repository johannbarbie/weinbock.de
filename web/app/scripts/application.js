define([
	'backbone',
	'communicator',
	'views/item/header',
	'views/item/footer',
	'views/item/space'
],

function( Backbone, Communicator, HeaderView, FooterView, SpaceView) {
    'use strict';

	var App = new Backbone.Marionette.Application();

	/* Add application regions here */
	App.addRegions({
		header: '#header',
		topRegion: '#top',
		contentRegion: '#content',
		footer: '#footer'
	});

	// marionette app events...
    App.on('initialize:after', function() {
        if (Backbone.history){
            Backbone.history.start();
        }
    });

    Communicator.mediator.on('app:show', function(topView,contentView) {
		if (topView){
			App.topRegion.show(topView);
		}else{
			App.topRegion.show(new SpaceView());
		}
		
		if (contentView){
			App.contentRegion.show(contentView);
		}else{
			App.contentRegion.close();
		}
    });

    Communicator.mediator.on('app:logout', function() {
		//renavigate to see if we still have permission
		App.router.navigate('logout',{trigger: true});
    });

	/* Add initializers here */
	App.addInitializer( function (options) {
		
		App.header.show(new HeaderView());
		App.footer.show(new FooterView());

		this.router = new options.pageController.Router({
            controller: options.pageController // wire-up the start method
        });

	});

	return App;
});
