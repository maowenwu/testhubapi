/**
 * 用户中心-菜单管理
*/
angular.module('inspinia').controller('rightAddMenuCtrl',function($scope,$http,$state,$stateParams,i18nService,$timeout){
	i18nService.setCurrentLang('zh-cn');
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	$scope.baseInfo = {};
	$scope.menuType = [{text:"菜单",value:"menu"},{text:"页面操作",value:"page"}];
	$scope.levelType = [{text:"0",value:0},{text:"1",value:1},{text:"2",value:2}];
	$scope.menuTypeAll = [{text:"全部",value:""},{text:"菜单",value:"menu"},{text:"页面操作",value:"page"}];
	$scope.menuData=[];
	$scope.paginationOptions.pageSize = 500;
	$scope.type=1;
	$scope.abc = {};
//	$scope.flag = true;
	$scope.menuGrid = {
		data: 'menuData',
		paginationPageSize: 500,
		paginationPageSizes: [500],
		useExternalPagination: true,		  	//开启拓展名
		 saveFocus: false,
		 saveScroll: true,
		 saveGroupingExpandedStates: true,
		columnDefs: [
//            {field: 'id',displayName: '菜单ID'},
            {field: 'menuName',width:280,cellClass:'align-left',displayName: '菜单名称'},
//            {field: 'menuCode', displayName: '菜单编码'},
//            {field: 'menuUrl', displayName: '菜单路径'},
//            {field: 'menuLevel',displayName: '菜单等级'},
            {field: 'menuType',width:330,displayName: '菜单类型', cellFilter:"formatDropping:"+ angular.toJson($scope.menuType)},
//            {field: 'parentId',displayName: '上级ID'},
//            {field: 'orderNo',displayName: '排序'}
        ],
        onRegisterApi: function(gridApi){	
        	$scope.gridApi = gridApi;
        	$scope.expandAll = function(){
    		    $scope.gridApi.treeBase.expandAllRows();
    		    $scope.flag = true;
    	 };
    	//选择事件
    	$scope.gridApi.selection.on.rowSelectionChanged($scope,function(row){
			var whatIndex = null;
   		angular.forEach($scope.rightMenus,function(data,index){
   			if(data.id==row.entity.id){
				whatIndex = index;
   			}
    		});
			$scope.rightMenus.splice(whatIndex,1);
    	});
        },
        isRowSelectable: function(row){ // 选中行 
        	if(!$scope.flag){
        		return;
        	}
        	$scope.flag = true;
			if($scope.rightMenus != null){
				for(var i=0;i<$scope.rightMenus.length;i++){
					 if(row.entity.id==$scope.rightMenus[i].id){
						 row.grid.api.selection.selectRow(row.entity);
					 }
				}
			}
			 
        },
	};
	
	$scope.baseInfo.id = $stateParams.id;
	$scope.baseInfo.rightName = $stateParams.rightName;
	//查询
	$scope.query = function(){
		$http.post('right/selectMenuByRight.do',"baseInfo="+angular.toJson($scope.baseInfo)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+
				$scope.paginationOptions.pageSize,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
				.success(function(msg){
					if(!msg){
						return;
					}
					$scope.menuData = msg.page.result;
					$scope.menuGrid.totalItems = msg.page.totalCount;
					$scope.rightMenus = msg.rightMenus;
					angular.forEach($scope.menuData,function(data){
						if(data.menuType=="menu" && data.menuLevel==0){
							data.$$treeLevel = 0;
						} else if(data.menuType=="menu" && data.menuLevel==1){
							data.$$treeLevel = 1;
							data.menuName = "--" + data.menuName;
						} else {
							data.menuName = "-----" + data.menuName;
						}
						if(data.orderNo!=null){
							if(data.menuLevel==0){
								data.orderNo = data.orderNo/1000000;
							}
							else if(data.menuLevel==1){
								data.orderNo = data.orderNo.substr(data.orderNo.length-5,2);
							} else if(data.menuLevel==2){
								data.orderNo = data.orderNo.substr(data.orderNo.length-3,3);
							}
						}
						$timeout($scope.expandAll);
						
					});
				}).error(function(){
				});
	}
	$scope.query();
	
	//提交菜单
	$scope.editMenu = function(){
		var menus = $scope.gridApi.selection.getSelectedRows();
		var menuIds = [];
		angular.forEach(menus,function(data,index){
			menuIds[index] = data.id;
		});
		$scope.submitting = true;
		var param = {"rightId":$stateParams.id,"menuIds":menuIds};
		$http.post("right/editRightMenu.do",angular.toJson(param))
			.success(function(msg){
				$scope.notice(msg.msg);
				$state.transitionTo('user.right',null,{reload:true});
				$scope.submitting = false;
			}).error(function(){
			$scope.submitting = false;
			});
	}
	//清空查询条件
	$scope.resetForm = function(){
		$scope.baseInfo = {};
	}
	
});