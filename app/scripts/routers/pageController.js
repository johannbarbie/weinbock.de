define(['backbone',
    'communicator', 
    'views/item/carousel',
    'views/item/marketing',
    'views/item/signin',
    'views/composite/productsView',
    'collections/products'
    ], function(Backbone, Communicator,  CarouselView, MarketingView, SignInView, ProductsView, Products ) {
    'use strict';

    var Controller = {};

    // private module/app router  capture route and call start method of our controller
    Controller.Router = Backbone.Marionette.AppRouter.extend({
        appRoutes: {
            '': 'showIndex',
            'signIn': 'showSignIn',
            'shop': 'showShop'
        }
    });

    Controller.showIndex = function() {
        var topView = new CarouselView();
        var contentView = new MarketingView();
        Communicator.mediator.trigger('app:show',topView,contentView);
    };

    Controller.showSignIn = function() {
        var topView = null;
        var contentView = new SignInView();
        Communicator.mediator.trigger('app:show',topView,contentView);
    };

    Controller.showShop = function() {
        var topView = null;
        var products = new Products([{id:'1'},{id:'2'}]);
        var contentView = new ProductsView({collection:products});
        Communicator.mediator.trigger('app:show',topView,contentView);
    };

    return Controller;
});