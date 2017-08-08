var addressApp = angular.module('addressApp', []);

addressApp.directive('fileModel', [ '$parse', function($parse) {
	return {
		restrict : 'A',
		link : function(scope, element, attrs) {
			var model = $parse(attrs.fileModel);
			var modelSetter = model.assign;

			element.bind('change', function() {
				scope.$apply(function() {
					modelSetter(scope, element[0].files[0]);
				});
			});
		}
	};
} ]);

addressApp.service('fileUpload', [ '$http', function($http) {
	this.uploadFileToUrl = function(file, uploadUrl, options) {
		var fd = new FormData();
		fd.append('file', file);

		$http.post(uploadUrl, fd, {
			transformRequest : angular.identity,
			headers : {
				'Content-Type' : undefined
			}
		})

			.success(function(data) {
				console.log("upload successful");
				if (options && options.successCallBack) {
					return options.successCallBack(data);
				}
			})

			.error(function() {
				console.log("upload error");
				if (options && options.failureCallBack) {
					return options.failureCallBack();
				}
			});
	}
} ]);

addressApp.controller('addressController', [ '$scope', 'fileUpload',
	function($scope, fileUpload) {
		var states = [ "AL", "AK", "AS", "AZ", "AR", "CA", "CO", "CT", "DE", "FM",
			"FL", "GA", "GU", "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME",
			"MH", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ",
			"NM", "NY", "NC", "ND", "MP", "OH", "OK", "OR", "PW", "PA", "PR", "RI",
			"SC", "SD", "TN", "TX", "UT", "VT", "VI", "VA", "WA", "WV", "WI", "WY" ];
		var states_set = new Set(states);
		var MAX_NAME_LENGTH = new Number(100);


		//validation function
		$scope.validate = function(address) {
			var errors = [];

			//state abbreviation validation
			if (!states_set.has(address.state)) {
				errors.push("Incorrect state abbreviation");
			}

			//zip code validation
			var zipPattern = new RegExp(/(^\d{5}-\d{4}?$)/);
			if (!zipPattern.test(address.zipCode)) {
				errors.push("Invalid zip code format");
			}

			//non-numeric character and domain validation
			var symbol = /[$-%(-,\.-/:-@{-~!"^_`\[\]]/;
			var domain = /.COM|.NET|.GOV|.EDU|.ORG/i;
			if (symbol.test(address.fname) && !(domain.test(address.fname))) {
				errors.push("Invalid non-numeric character in first name");
			}
			if (symbol.test(address.lname) && !(domain.test(address.lname))) {
				errors.push("Invalid non-numeric character in last name");
			}
			if (symbol.test(address.company) && !(domain.test(address.company))) {
				errors.push("Invalid non-numeric character in company name");
			}

			//carriage returns validation
			var carriage = /^[^\t|\n|\r]*$/;
			if (!carriage.test(address.fname)) {
				errors.push("Invalid carriage return in first name");
			}
			if (!carriage.test(address.lname)) {
				errors.push("Invalid carriage return in last name");
			}
			if (!carriage.test(address.company)) {
				errors.push("Invalid carriage return in company name");
			}

			//upper case name validation
			if (!((address.fname).toUpperCase() === (address.fname))) {
				errors.push("First name not upper case");
			}
			if (!((address.lname).toUpperCase() === (address.lname))) {
				errors.push("Last name not upper case");
			}
			if (!((address.company).toUpperCase() === (address.company))) {
				errors.push("Company name not upper case");
			}

			//max length validation
			if ((address.fname).length > MAX_NAME_LENGTH) {
				errors.push("Invalid first name length ");
			}
			if ((address.lname).length > MAX_NAME_LENGTH) {
				errors.push("Invalid last name length ");
			}
			if ((address.company).length > MAX_NAME_LENGTH) {
				errors.push("Invalid company name length ");
			}

			//non-latin validation
			var latinchars = /^[\u0000-~\u0080-\u00FF]*$/;
			if (!(latinchars.test(address.fname))) {
				errors.push("Non-latin characters in first name");
			}
			if (!(latinchars.test(address.lname))) {
				errors.push("Non-latin characters in last name");
			}
			if (!(latinchars.test(address.company))) {
				errors.push("Non-latin characters in company name");
			}

			console.log(address);
			console.log(errors);
			return errors;
		};

		//upload file function
		$scope.uploadFile = function() {
			var file = $scope.myFile;
			$scope.headers = false;
			$scope.fileError = false;

			if (file != null) {
				//console.dir(file);
				
				//var uploadUrl = "/uploadFile"; //boot app
				var uploadUrl = "/addressqualityvalidation/uploadFile"; //server
				fileUpload.uploadFileToUrl(file, uploadUrl, {
					successCallBack : successHandler,
					failureCallBack : failureHandler
				});

				function successHandler(data) {
					$scope.addresses = data;
					$scope.headers = true;
				}

				function failureHandler() {
					console.log("upload failed");
				}
			}
			else {
				$scope.fileError = true;
			}
		};
	} ]);