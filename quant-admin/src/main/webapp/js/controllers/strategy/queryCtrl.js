/**
 * 配置中心-对冲配置管理 
*/
angular.module('inspinia',['uiSwitch']).controller('queryCtrl',function($scope,$http,$state,$stateParams,i18nService,SweetAlert,$document){
	i18nService.setCurrentLang('zh-cn');
	$scope.hedgeGrid = {
		data: 'queryData',
		enableSorting: true,
		useExternalPagination: true,
		enableHorizontalScrollbar: 0,
		enableVerticalScrollbar: 0,
		columnDefs: [
            {field: 'symbol', displayName: '账户组别'},
            {field: 'contractType', displayName: '运行状态'},
            {field: 'hedgeInterval', displayName: '总盈亏'},
            {field: 'buySlippage', displayName: '合约代码'},
            {field: 'id', displayName: '操作', cellTemplate:
            	'<div class="lh30">  <a ng-click="grid.appScope.toDetail(row.entity.id)">详情</a> </div>'
            }
        ]

	};
	
	//查询
	$scope.query = function(){
		$http.post('test/test1.do',"",{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
				.success(function(list){
					if(!list){
						return;
					}
					$scope.queryData =list;
				}).error(function(){
				});
	}
	$scope.query();


    //修改任务
    $scope.toDetail = function(entity){
        $state.go('strategy.queryDetail', {param:angular.toJson(entity)});
    }


    $scope.queryDetailxxxx = function(id){
        $http.post('test/test1.do',"",{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(list){
                if(!list){
                    return;
                }
                $scope.queryData =list;
            }).error(function(){
        });
    }



	






	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.query();
			}
		})
	});
});