(function () {
	'use strict';
	angular.module('profession-picker')
		.controller('PictureDisplayController', PictureDisplayController);

	PictureDisplayController.$inject = ['$scope', 'DropboxService'];

	function PictureDisplayController($scope, DropboxService) {
		var url = serverConfig.SERVER();
		var vm = this;
		vm.pictures = getPictures();
		vm.deletePicture = removePicture;
		vm.uploadFile = uploadFile;

		function getPictures() {
			DropboxService.getPictures
				.then(function (response) {
					vm.pictures = response.data;
					var addDtoParameters = function (picture) {
						picture.ratioClass = (picture.ratio > 1.45 ? "file bigfile" : "file"); // (b?x:y) parens fix bug
						picture.urlLink = url + 'picture/' + picture.id + '.jpg';
					};
					vm.pictures.forEach(addDtoParameters);
				});
		}

		function removePicture(id) {
			DropboxService.removePicture(id)
				.then(function () {
					location.reload();
				})
		}

		function uploadFile() {
			var file = $scope.myFile;
			if (file != undefined || file != null) {
				DropboxService.uploadPicture(file)
					.then(function () {
						location.reload();
					})
			}
		}
	}
})();