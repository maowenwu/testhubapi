/**
 * 配置中心-对冲配置管理
 */
angular.module('inspinia', ['uiSwitch']).controller('queryCtrl', function ($scope, $http, $state, $stateParams, i18nService, SweetAlert, $document) {
    i18nService.setCurrentLang('zh-cn');
    $scope.selectedCoin = "";
    $scope.strategyInstanceInfoGrid = {
        data: 'queryData',
        enableSorting: true,
        useExternalPagination: true,
        enableHorizontalScrollbar: 0,
        enableVerticalScrollbar: 0,
        columnDefs: [
            {field: 'instanceGroup', displayName: '账户组别'},
            {field: 'instanceEnable', displayName: '运行状态'},
            {field: 'totalProfit', displayName: '总盈亏'},
            {field: 'futureContractCode', displayName: '合约代码'},
            {
                field: 'id', displayName: '操作', cellTemplate:
                    '<div class="lh30">  <a ng-click="grid.appScope.toDetail(row.entity.id)">详情</a> </div>'
            }
        ]

    };


    //查询  币种下拉框
    $scope.querySymbolSelect = function () {
        $http.post('sysDict/selectListByType.do', "type=coin_type", {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function (list) {
                if (!list) {
                    return;
                }
                list.foreach
                $scope.coinList = list;
            }).error(function () {
        });
    }
    $scope.querySymbolSelect();


    //查询  策略信息
    $scope.query = function () {
        debugger;
        var selectedCoin = $scope.selectedCoin;
        if (selectedCoin == null || selectedCoin == "") {
            return;
        }

        $http.post('common/findStrategyInstanceInfo.do', "futureBaseCoin=" + selectedCoin, {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function (reps) {
                if (!reps) {
                    return;
                }

                $scope.queryData = reps;
                $scope.queryData.forEach(function (item) {
                    if (item.instanceEnable == 1) {
                        item.instanceEnable = '正常';
                    } else {
                        item.instanceEnable = '停止';
                        item.contractCode="-";
                    }
                })

            }).error(function () {
        });
    }
    $scope.query();


    //跳转到详情
    $scope.toDetail = function (id) {
        //传id
        $state.go('strategy.queryDetail', {param: angular.toJson(id)});
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