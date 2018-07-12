/**
 * 用户新增
 */
angular.module('inspinia').controller('addUserCtrl',function($scope,$http,$state,$stateParams,$compile,$filter,i18nService){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文

	$scope.commit=function(){
		$scope.submitting = true;
		$http.post("user/addInfo",angular.toJson($scope.info))
		.success(function(data){
			if(data.bols){
				$scope.notice(data.msg);
				$state.go('user.userTotal');
			}else{
				$scope.notice(data.msg);
				$scope.submitting = false;
			}
		});
	}
})