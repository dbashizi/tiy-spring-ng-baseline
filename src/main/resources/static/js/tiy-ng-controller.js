angular.module('TIYAngularApp', [])
   .controller('SampleController', function($scope, $http) {

        console.log("I'm getting executed now!");

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

        $scope.toggleGame = function(gameID) {
            console.log("About to toggle game with ID " + gameID);

            $http.get("/toggleGame.json?gameID=" + gameID)
                .then(
                    function success(response) {
                        console.log(response.data);
                        console.log("Game toggled");

                        $scope.games = response.data;
                    },
                    function error(response) {
                        console.log("Unable to toggle game");
                    });
        };

        $scope.addGame = function() {
            console.log("About to add the following game " + JSON.stringify($scope.newGame));

            $http.post("/addGame.json", $scope.newGame)
                .then(
                    function successCallback(response) {
                        console.log(response.data);
                        console.log("Adding data to scope");
                        $scope.games = response.data;
                    },
                    function errorCallback(response) {
                        console.log("Unable to get data");
                    });
        };

        $scope.testFunction = function() {
            console.log("Testing testing 123");
            $scope.anotherFunction();
        };

        $scope.anotherFunction = function() {
            console.log("anotherFunction()");
        }

        $scope.name = "James";
        $scope.testVar = {};
        $scope.testVar.sampleContainerVar = "See, it's real!!!!";
        $scope.testVar.anotherSample = 12;
        $scope.testVar.flagExample = true;
        $scope.testVar.somethingThatDoesntExist = "it does exist!!!";

        console.log("Initializing newGame now");
        $scope.newGame = {};

    });
