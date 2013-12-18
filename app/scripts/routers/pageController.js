define(['backbone',
    'communicator',
    'views/item/carousel',
    'views/item/marketing',
    'views/item/signupView',
    'views/item/resetView',
    'views/item/resetConfView',
    'views/item/signupConfView',
    'views/item/loginView',
    'views/item/accountView',
    'models/accountRequest',
    'models/reset',
    'models/resetConf',
    'models/signupConf',
    'models/loginModel',
    'models/accountModel',
    'views/composite/productsView',
    'views/item/captchaView',
    'collections/products',
    'routeFilter'
    ], function(Backbone, Communicator,  CarouselView, MarketingView, SignUpView, ResetView, ResetConfView, SignupConfView, LoginView, AccountView, AccountRequest, Reset, ResetConf, SignupConf, LoginModel, AccountModel, ProductsView, CaptchaView, Products) {
    'use strict';

    var Controller = {};

    // private module/app router  capture route and call start method of our controller
    Controller.Router = Backbone.Marionette.AppRouter.extend({
        appRoutes: {
            '': 'showIndex',
            'signUp': 'showSignUp',
            'account': 'showAccount',
            'confSignup/:token': 'confirmSignUp',
            'confReset/:token': 'confirmReset',
            'reset': 'showReset',
            'shop': 'showShop'
        },
        before:{
            'signUp': 'getTicket',
            'reset': 'getTicket',
            'account': 'showLogin',
            '*any': function(fragment, args, next){
                console.log('before');
                next();
            }
        },
        getTicket: function(fragment, args, next) {
            if (!this.options.controller.ticket){
                //TODO: show wain screen
                var self = this;
                $.post( window.opt.basePath + '/account/ticket', function( data ) {
                    self.options.controller.ticket = data.value;
                    next();
                },'json').fail(function() {
                    var view = new CaptchaView({next:next,controller:self.options.controller});
                    Communicator.mediator.trigger('app:show', null, view);
                });
            }else{
                next();
            }
        },
        showLogin: function(fragment, args, next) {
            if (!this.options.controller.loginStatus){
                this.options.controller.loginStatus = new LoginModel();
            }
            var view;
            var model = this.options.controller.loginStatus;
            if (model.get('roles')){
                next();
            }else{
                view = new LoginView({model:model,next:next});
                Communicator.mediator.trigger('app:show', null, view);
            }
        }
    });

    Controller.showIndex = function() {
        var topView = new CarouselView();
        var contentView = new MarketingView();
        Communicator.mediator.trigger('app:show',topView,contentView);
    };

    Controller.showAccount = function() {
        var account = new AccountModel();
        var contentView = new AccountView({model:account});
        Communicator.mediator.trigger('app:show',null,contentView);
        account.fetch();
    };

    Controller.showSignUp = function() {
        var accountRequest = new AccountRequest({ticket:Controller.ticket});
        var contentView = new SignUpView({model:accountRequest});
        Communicator.mediator.trigger('app:show',null,contentView);
    };
    Controller.confirmSignUp = function(token) {
        var model = new SignupConf({token:token});
        var contentView = new SignupConfView({model: model});
        Communicator.mediator.trigger('app:show',null,contentView);
        model.save();
    };
    Controller.showReset = function() {
        var model = new Reset({ticket:Controller.ticket});
        var contentView = new ResetView({model:model});
        Communicator.mediator.trigger('app:show',null,contentView);
    };
    Controller.confirmReset = function(token) {
        var model = new ResetConf({token:token});
        var contentView = new ResetConfView({model: model});
        Communicator.mediator.trigger('app:show',null,contentView);
    };
    Controller.showShop = function() {
        var products = new Products([{id:'1'},{id:'2'}]);
        var contentView = new ProductsView({collection:products});
        Communicator.mediator.trigger('app:show',null,contentView);
    };

    return Controller;
});