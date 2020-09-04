(function ($) {
	"use strict";
    jQuery(document).ready(function($){
        /*--------------------
            wow js init
        ---------------------*/
        new WOW().init();

        /*=======================================
            banner carousel
        ========================================*/
        var bannerCarousel = $('.banner-carousel');
            bannerCarousel.owlCarousel({
            loop:true,
            dots:true,
            dotData:true,
            startPosition:2,
            nav:true,
            item: 1,
            navText: ['<i class="fas fa-angle-left"></i>','<i class="fas fa-angle-right"></i>'],
            autoplay:true,
            autoplayTimeout:2000,
            autoplayHoverPause:true,
            responsive : {
            0 : {
                items: 1
            },
            768 : {
                items: 1
            },
            960 : {
                items: 1
            },
            1200 : {
                items: 1
            },
            1920 : {
                items: 1
            }
            }
        }); 

        /*=======================================
            testimonial carousel
        ========================================*/
        var testimonialCarousel = $('.testimonial-slider');
        testimonialCarousel.owlCarousel({
        loop:true,
        dots:true,
        nav: false,
        margin: 30,
        autoplay:true,
        autoplayTimeout:3000,
        autoplayHoverPause:true,
        responsive : {
                0 : {
                    items: 1
                },
                768 : {
                    items: 2
                },
                960 : {
                    items: 2
                },
                1200 : {
                    items: 2
                },
                1920 : {
                    items: 2
                }
            }
        }); 



        /*=======================================
            brand carousel
        ========================================*/
        var bannerCarousel = $('.brand-carousel');
        bannerCarousel.owlCarousel({
            loop: true,
            dots: true,
            dotData: true,
            startPosition: 2,
            nav: false,
            margin: 30,
            item: 1,
            navText: ['<i class="fas fa-angle-left"></i>', '<i class="fas fa-angle-right"></i>'],
            autoplay: true,
            autoplayTimeout: 2000,
            autoplayHoverPause: true,
            responsive: {
                0: {
                    items: 1
                },
                768: {
                    items: 2
                },
                960: {
                    items: 3
                },
                1200: {
                    items: 4
                },
                1920: {
                    items: 4
                }
            }
        }); 

        /*=======================================
            property slider
        ========================================*/
        var bannerCarousel = $('.property-slider');
        bannerCarousel.owlCarousel({
            loop: true,
            dots: true,
            dotData: true,
            startPosition: 2,
            nav: true,
            item: 1,
            navText: ['<i class="fas fa-angle-left"></i>', '<i class="fas fa-angle-right"></i>'],
            autoplay: true,
            autoplayTimeout: 2000,
            autoplayHoverPause: true,
            responsive: {
                0: {
                    items: 1
                },
                768: {
                    items: 1
                },
                960: {
                    items: 1
                },
                1200: {
                    items: 1
                },
                1920: {
                    items: 1
                }
            }
        });

        //======================================
        //========== magnificPopup video ============
        //======================================
        $('.venobox').magnificPopup({
            type: 'video'
        });
        $('.image-popup').magnificPopup({
            type: 'image'
        }); 
        
 
    });

    //define variable for store last scrolltop
    var lastScrollTop = '';
    $(window).on('scroll', function () {
        //back to top show/hide
       var ScrollTop = $('.back-to-top');
       if ($(window).scrollTop() > 1000) {
           ScrollTop.fadeIn(1000);
       } else {
           ScrollTop.fadeOut(1000);
       }
       /*--------------------------
        sticky menu activation
       -------------------------*/
        var st = $(this).scrollTop();
        var mainMenuTop = $('.header');
        if ($(window).scrollTop() > 300) {
            if (st > lastScrollTop) {
                // hide sticky menu on scrolldown 
                mainMenuTop.removeClass('nav-fixed');
                
            } else {
                // active sticky menu on scrollup 
                mainMenuTop.addClass('nav-fixed');
            }

        } else {
            mainMenuTop.removeClass('nav-fixed ');
        }
        lastScrollTop = st;
       
    });
           
    $(window).on('load',function(){
        /*-----------------
            preloader
        ------------------*/
        var preLoder = $(".preloader");
        preLoder.fadeOut(1000);
    });

}(jQuery));	




