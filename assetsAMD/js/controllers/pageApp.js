define(['marionette', 'views/indexView', 'vent'], function(Marionette, IndexView, vent) {
    'use strict';

    var Controller = {};

    // private module/app router  capture route and call start method of our controller
    Controller.Router = Marionette.AppRouter.extend({
        appRoutes: {
            "": "showIndex"
        }
    });

    // Start the app by showing the appropriate views    
    Controller.showIndex = function() {
        var view = new IndexView();
        vent.trigger('app:show', view);
    };

    return Controller;
});