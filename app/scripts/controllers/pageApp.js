define(['marionette', 'views/indexView', 'views/aboutView', 'views/howtoView', 'views/faqView', 'vent'], function(Marionette, IndexView, AboutView, HowtoView, FaqView, vent) {
    'use strict';

    var Controller = {};

    // private module/app router  capture route and call start method of our controller
    Controller.Router = Marionette.AppRouter.extend({
        appRoutes: {
            '': 'showIndex',
            'about-us': 'showAbout',
            'howto': 'showHowto',
            'faq': 'showFaq'
        }
    });

    // Start the app by showing the appropriate views    
    Controller.showIndex = function() {
        var view = new IndexView();
        vent.trigger('app:show', view);
    };

    Controller.showAbout = function() {
        var view = new AboutView();
        vent.trigger('app:show', view);
    };

    Controller.showHowto = function() {
        var view = new HowtoView();
        vent.trigger('app:show', view);
    };

    Controller.showFaq = function() {
        var view = new FaqView();
        vent.trigger('app:show', view);
    };

    return Controller;
});