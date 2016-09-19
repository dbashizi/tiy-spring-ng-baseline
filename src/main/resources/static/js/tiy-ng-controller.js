angular.module('TIYAngularApp', [])
   .controller('SampleController', function($scope, $http) {
        $scope.name = "James";
        $scope.testVar = {};
        $scope.testVar.sampleContainerVar = "See, it's real!!!!";
        $scope.testVar.anotherSample = 12;
        $scope.testVar.flagExample = true;
        $scope.testVar.somethingThatDoesntExist = "it does exist!!!";

        $scope.getGames = function() {
            console.log("About to go get me some data!");
            $scope.name = "JavaScript Master Guru";

            $http.get("http://localhost:8080/games.json")
                .then(
                    function successCallback(response) {
                        console.log(response.data);
                        console.log("Adding data to scope");
                        $scope.games = response.data;
                    },
                    function errorCallback(response) {
                        console.log("Unable to get data");
                    });

            console.log("Done with the promise - waiting for the callback");
        };

        $scope.newGame = {};
    });
