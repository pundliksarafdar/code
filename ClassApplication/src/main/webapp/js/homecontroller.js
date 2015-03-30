var homepageapp = angular.module('homepageapp',[]);

homepageapp.controller('myhomecontroller',function($scope){
	$scope.headerUrl = "home_head.html";
	$scope.carousel = "carousel.html";
	$scope.footer = ""
	
	$scope.showMenuData = function(dataUrl){
		$scope.showdivurl= dataUrl+".html";
	}
	
	$scope.clearMenu = function(){
		$scope.showdivurl = null;	
	}
});