// LibraryRouter.js  hookup the default "" and search routes for the library
define(['marionette'], function(Marionette) {
    'use strict';

    var LibraryRouting = {};

    LibraryRouting.Router = Marionette.AppRouter.extend({
        appRoutes: {
            'default': 'defaultSearch',
            'search/:searchTerm': 'search'
        }
    });

    return LibraryRouting;
});