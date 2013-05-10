define(['underscoreM', 'marionette', 'templates'], function(_, Marionette, templates) {
    'use strict';
    return Marionette.ItemView.extend({
        className: 'fromTemplate',
        template: _.template(templates.faq),
        initialize: function(opt) {
			this.loc = opt.loc;
        },
        events: {
            'click #navbar': 'click'
        },
        click:function (e){
            e.preventDefault();
            $('html,body').animate({scrollTop: $($(e.target).data('wbtarget')).offset().top},'slow');
        },
        onShow:function () {
            if ($('#navbar')){
                $('#navbar').affix({offset:207});
            }
            if ($('#faqContent')){
                $('body').scrollspy({target: '#navContainer'});
            }
        }
    });
});