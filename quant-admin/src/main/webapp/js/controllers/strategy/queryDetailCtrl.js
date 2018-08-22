/**
 * 配置中心-期货用户详情
 */
angular.module('inspinia', ['uiSwitch']).controller('queryDetailCtrl', function ($scope, $http, $state, $stateParams, i18nService, SweetAlert, $document) {
    i18nService.setCurrentLang('zh-cn');
    $scope.strategyProfitCountShow = true;
    $scope.strategyRiskCountShow = false;


    //查询基本信息
    $scope.baseInfo = function () {
        var id = $stateParams.param;
        $http.post('common/findStrategyInstanceBaseInfo.do', "id=" + id, {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function (resp) {
                if (!resp) {
                    return;
                }
                $scope.baseInfoData = resp;
            }).error(function () {
        });
    }
    $scope.baseInfo();


    //查询账户信息
    $scope.accountInfo = function () {
        var id = $stateParams.param;
        $http.post('common/findAccountInfo.do', "id=" + id, {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function (resp) {
                if (!resp) {
                    return;
                }
                $scope.accountInfoData = resp;
            }).error(function () {
        });
    }
    $scope.accountInfo();


    //策略 收益统计
    $scope.strategyProfitCount = function () {
        debugger;
        var id = $stateParams.param;
        $scope.strategyProfitCountShow = true;
        $scope.strategyRiskCountShow = false;
        $http.post('common/strategyProfitCount.do', "id="+id, {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function (resp) {
                if (!resp) {
                    return;
                }
                $scope.strategyProfitCountData = resp;
            }).error(function () {
        });
    }

    // 默认查询  策略 收益统计
    $scope.strategyProfitCount();

    //策略 风控统计
    $scope.strategyRiskCount = function () {
        $scope.strategyProfitCountShow = false;
        $scope.strategyRiskCountShow = true;
        $http.post('common/strategyRiskCount.do', "id=1", {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function (resp) {
                if (!resp) {
                    return;
                }
                $scope.strategyRiskCountData = resp;
                debugger;
            }).error(function () {
        });
    }


    //页面绑定回车事件
    $document.bind("keypress", function (event) {
        $scope.$apply(function () {
            if (event.keyCode == 13) {
                $scope.query();
            }
        })
    });
});