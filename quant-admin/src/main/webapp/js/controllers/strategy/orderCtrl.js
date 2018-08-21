/**
 * 配置中心-摆单/对冲风控配置管理
 */
angular.module('inspinia', ['uiSwitch']).controller('orderTestCtrl', function ($scope, $http, $state, $stateParams, i18nService, SweetAlert, $document) {
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions = angular.copy($scope.paginationOptions);
    $scope.queryOrderConfig = "";

    $scope.orderSymbols = [
        {key : "BTC", value : "BTC"},
        {key : "ETH", value : "ETH"},
        {key : "LTC", value : "LTC"},
        {key : "aaa", value : "test"}
    ];


    //查询摆单配置      摆单开始===========================================
    $scope.queryOrderConfig = function () {
        var symbol = $scope.queryOrderConfig.symbol;
        var contractType = $scope.queryOrderConfig.contractType;
        if (symbol == null || "" == symbol) {
            symbol = "BTC";
        }
        if (contractType == null || "" == contractType) {
            contractType = "this_week";
        }
        $http.post(
            'order/selectBySymbolContractType.do', 'symbol=' + symbol + '&contractType=' + contractType, {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function (resp) {
                if (!resp) {
                    $scope.strategyOrderConfig = resp;
                    return;
                }
                $scope.strategyOrderConfig = resp;
                $scope.newInfo = resp;

            }).error(function () {
        });
    }

    //查询摆单配置
    $scope.queryOrderConfig();


    //修改摆单配置
    $scope.editOrderConfig = function (e, key) {
        var td = $(e.target);
        td = $(td.parent());
        td = $(td.prev());
        var txt = td.text();
        var input = $("<input type='text' value='" + txt + "' style='width:82px;height:26px;'/>");
        td.html(input);
        input.click(function () {
            return false;
        });
        //获取焦点
        input.trigger("focus");
        //文本框失去焦点后提交内容，重新变为文本
        input.blur(function () {
            var newtxt = $(this).val();
            //判断文本有没有修改
            if (newtxt != txt) {
                var result = confirm("是否要修改？");
                if (result) {
                    var id = $scope.strategyOrderConfig.id;
                    var data = "{'" + key + "':" + newtxt + ";'id':" + id + "}";
                    $http.post(
                        'order/updateOrder.do', "newInfo=" + data, {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function (resp) {
                            alert(resp.msg)
                        }).error(function () {
                        alert(resp.toString())
                    });
                }
                td.html(newtxt);
            }
            else {
                td.html(newtxt);
            }
        });
    }

    //摆单结束===========================================


    //查询对冲配置      对冲开始===========================================
    $scope.queryHedgeConfig = function () {
        var symbol = $scope.hedgeSymbol;

        var contractType = $scope.hedgeContractType;
        if (symbol == null || "" == symbol) {
            symbol = "BTC";
        }
        if (contractType == null || "" == contractType) {
            contractType = "this_week";
        }
        $http.post(
            'hedge/selectBySymbolContractType.do', 'symbol=' + symbol + '&contractType=' + contractType, {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function (resp) {
                if (!resp) {
                    $scope.strategyHedgeConfig = resp;
                    return;
                }
                $scope.strategyHedgeConfig = resp;
            }).error(function () {
        });
    }

    //查询摆单配置
    $scope.queryHedgeConfig();


    //修改摆单配置
    $scope.editHedgeConfig = function (e, key) {
        var td = $(e.target);
        td = $(td.parent());
        td = $(td.prev());
        var txt = td.text();
        var input = $("<input type='text' value='" + txt + "' style='width:82px;height:26px;'/>");
        td.html(input);
        input.click(function () {
            return false;
        });
        //获取焦点
        input.trigger("focus");
        //文本框失去焦点后提交内容，重新变为文本
        input.blur(function () {
            var newtxt = $(this).val();
            //判断文本有没有修改
            if (newtxt != txt) {
                var result = confirm("是否要修改？");
                if (result) {
                    var id = $scope.strategyHedgeConfig.id;
                    var data = "{'" + key + "':" + newtxt + ";'id':" + id + "}";
                    $http.post(
                        'hedge/updateHedge.do', "newInfo=" + data, {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function (resp) {
                            alert(resp.msg)
                        }).error(function () {
                        alert(resp.toString())
                    });
                }
                td.html(newtxt);
            }
            else {
                td.html(newtxt);
            }
        });
    }

    //对冲结束===========================================


    //查询风控配置      风控开始===========================================
    $scope.queryRiskConfig = function () {
        var symbol = $scope.riskSymbol;
        var contractType = $scope.riskContractType;
        if (symbol == null || "" == symbol) {
            symbol = "BTC";
        }
        if (contractType == null || "" == contractType) {
            contractType = "this_week";
        }
        $http.post(
            'risk/selectBySymbolContractType.do', 'symbol=' + symbol + '&contractType=' + contractType, {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function (resp) {
                if (!resp) {
                    $scope.strategyRiskConfig = resp;
                    return;
                }
                $scope.strategyRiskConfig = resp;
            }).error(function () {
        });
    }

    //查询风控配置
    $scope.queryRiskConfig();


    //修改风控配置
    $scope.editRiskConfig = function (e, key) {
        var td = $(e.target);
        td = $(td.parent());
        td = $(td.prev());
        var txt = td.text();
        var input = $("<input type='text' value='" + txt + "' style='width:82px;height:26px;'/>");
        td.html(input);
        input.click(function () {
            return false;
        });
        //获取焦点
        input.trigger("focus");
        //文本框失去焦点后提交内容，重新变为文本
        input.blur(function () {
            var newtxt = $(this).val();
            //判断文本有没有修改
            if (newtxt != txt) {
                var result = confirm("是否要修改？");
                if (result) {
                    var id = $scope.strategyRiskConfig.id;
                    var data = "{'" + key + "':" + newtxt + ";'id':" + id + "}";
                    $http.post(
                        'order/updateOrder.do', "newInfo=" + data, {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function (resp) {
                            alert(resp.msg)
                        }).error(function () {
                        alert(resp.toString())
                    });
                }
                td.html(newtxt);
            }
            else {
                td.html(newtxt);
            }
        });
    }

    //风控结束===========================================


    //页面绑定回车事件
    $document.bind("keypress", function (event) {
        $scope.$apply(function () {
            if (event.keyCode == 13) {
                $scope.query();
            }
        })
    });


});