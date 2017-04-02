(function () {
	'use strict';
	angular.module('profession-picker')
		.service('DropboxService', DropboxService);

	DropboxService.$inject = ['$http'];

	function DropboxService($http) {
		var url = serverConfig.SERVER();
		return {
			getPictures: getPictures(),
			removePicture: removePicture,
			uploadPicture: uploadPicture
		};

		function getPictures() {
			return $http.get(url + 'files');
		}

		function removePicture(id) {
			return $http.post(url + 'remove/' + id);
		}

		function uploadPicture(files) {
			var data = new FormData();
			for (var i = 0; i < files.length; i++) {
				data.append("file", files[i]);
			}
			return $http.post(url + 'add', data, {
				transformRequest: angular.identity,
				headers: {'Content-Type': undefined}
			});
		}
	}
})();