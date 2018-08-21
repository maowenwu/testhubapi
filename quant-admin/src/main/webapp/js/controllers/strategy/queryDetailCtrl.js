/**
 * 配置中心-期货用户详情
 */
angular.module('inspinia', ['uiSwitch']).controller('queryDetailCtrl', function ($scope, $http, $state, $stateParams, i18nService, SweetAlert, $document) {
    i18nService.setCurrentLang('zh-cn');
    $scope.profitCountData = {xxx:2,yyy:3,zzz:4};
    $scope.riskCountData = {xxx:5,yyy:6};
    $scope.accountInfoGrid = {
        data: 'accountInfoData',
        enableSorting: true,
        useExternalPagination: true,
        enableHorizontalScrollbar: 0,
        enableVerticalScrollbar: 0,
        columnDefs: [
            {field: 'symbol', displayName: '交易所'},
            {field: 'contractType', displayName: '账户ID'},
            {field: 'hedgeInterval', displayName: '持仓量（张）'},
            {field: 'buySlippage', displayName: '持币量 （BTC）'},
            {field: 'hedgeInterval', displayName: '资金量（USDT）'},
            {
                field: 'id', displayName: '操作', cellTemplate:
                    '<div class="lh30"><a ng-show="grid.appScope.hasPermit(\'spotJob.update\')"  ng-click="grid.appScope.editModal(row.entity)">充</a>'
                +'|<a ng-show="grid.appScope.hasPermit(\'spotJob.update\')"  ng-click="grid.appScope.editModal(row.entity)">提</a>'
            }
        ]
    };

    $scope.profitCountShow=true;
    $scope.riskCountShow=false;


    //查询基本信息
    $scope.baseInfo = function () {
        $http.post('hedge/test2.do', "id=1", {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function (resp) {
                if (!resp) {
                    return;
                }
                $scope.baseInfoData = resp;
                debugger;
            }).error(function () {
        });
    }
    $scope.baseInfo();


    //查询账户信息
    $scope.accountInfo = function () {
        $http.post('test/test1.do', "id=1", {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function (resp) {
                if (!resp) {
                    return;
                }
                $scope.accountInfoData = resp;
            }).error(function () {
        });
    }
    $scope.accountInfo();


    //收益统计
    $scope.profitCount = function () {
        $scope.profitCountShow=true;
        $scope.riskCountShow=false;
        $http.post('hedge/test2.do', "id=1", {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function (resp) {
                if (!resp) {
                    return;
                }
                $scope.profitCountData =  {xxx:2,yyy:3,zzz:4};
                debugger;
            }).error(function () {
        });
    }

    //收益统计
    $scope.riskCount = function () {
        $scope.profitCountShow=false;
        $scope.riskCountShow=true;
        $http.post('hedge/test2.do', "id=1", {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function (resp) {
                if (!resp) {
                    return;
                }
                $scope.riskCountData = {xxx:2,yyy:3,zzz:4};
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