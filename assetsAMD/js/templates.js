// templates
define(function(require) {
    "use strict";
    return {
        book: require('text!templates/book.htm'),
        bookdetail: require('text!templates/bookdetail.htm'),
        booklist: require('text!templates/booklist.htm'),
        librarylayout: require('text!templates/librarylayout.htm'),
        index: require('text!templates/index.htm'),
        header: require('text!templates/header.htm'),
        footer: require('text!templates/footer.htm'),
        about: require('text!templates/about.htm'),
        howto: require('text!templates/howto.htm')
    };
});
