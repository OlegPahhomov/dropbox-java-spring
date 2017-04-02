(function () {
	'use strict';
	angular
		.module('profession-picker', [
			'ngRoute'
		])
		.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
			var urlBase = "/resources/";
			$locationProvider.hashPrefix('');
			$routeProvider
				.when("/", {
					templateUrl: urlBase + "esileht.html",
					controller: 'PictureDisplayController as vm'
				})
				.otherwise({redirectTo:'/'})
		}]);
})();