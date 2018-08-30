$(function(){
	$.ajax({
		url:"diskname",//请求返回盘符                                                                                   //通过              
		type:"post",
		success:function(data){
			var ajson=eval("("+data+")");
			var item;
			$.each(ajson,function(key,value){
				item="<option value="+value+">"+value+"</option>"
				$("#root").append(item);
			});
			$("#root").append("<option value='全盘'>全盘</option>");
		}
	})
		var search=$("input[name='searchtype']");
		for(var i=0;i<search.length;i++){
			search[i].onclick=function(){
				//alert(this.value);//为什么一定要是this.value 不可以是search[i].val()
                  showContent(this.value);
				}
		}
})
//function test(){
//	alert("msjfbd");
//	var search=$("input[name='searchtype']");
//	for(var i=0;i<search.length;i++){
//		search[i].onclick=function(){
//			alert(search[i].val());
//              showContents(this.value);
//			}
//	}
//}
function reset(){                                        //通过
			$("#filename").val("");
			$("input[type='radio']").val('');
		}
function searchSort(radio){
			if($("input[name='searchsort']:checked").val()=="Vague"){
				$("#vague").prop("checked",true);
//				alert(radio.checked);
			    if (radio.tag==1)
			    {
			        radio.checked=false;
			        radio.tag=0;
//			        alert(radio.checked);
			    }
			    else
			    {
			        radio.checked=true;
			        radio.tag=1
			    }
			}
			else if($("input[name='searchsort']:checked").val()=="Type"){
				$("#type").prop("checked",true);
			    if (radio.tag==1)
			    {
			        radio.checked=false;
			        radio.tag=0;
			    }
			    else
			    {
			        radio.checked=true;
			        radio.tag=1
			    }
			}
			else if($("input[name='searchsort1']:checked").val()=="Rebuild"){
				$("#rebuild").prop("checked",true);
				 if (radio.tag==1)
				    {
				        radio.checked=false;
				        radio.tag=0;
				    }
				    else
				    {
				        radio.checked=true;
				        radio.tag=1
				    }
			}
		}
function showContent(searchType){
	var filename=$("#filename").val();
	var vague=$("#vague").prop("checked");//true or false 判断是否选中
	var index=$("#rebuild").prop("checked");//true or false 判断是否选中
	var type=$("#type").prop("checked");//true or false
	var root=$("#root").val();
	var td;
	if(typeof(searchType)!="undefined"){
		$.ajax({
			url:"fileIndex",//当点击媒体类型时发起请求，媒体类型（文件类型）发送给后台
			data:{'root':root,
				'vague':vague,
				'type':type,
				'filename':filename,
				'filetype':searchType,
				'index':index
				},
			type:"get",
			beforeSend:function(){
				showPic();
			},
			success:function(data){	
				var ajson=eval("("+data+")");
				var Ist=ajson[0];
				var item;
				clearBox();
				$.each(ajson,function(key,value){
					item="<tr><td>"+value+"<tr><td>";//ajson对象的什么数据
					$("#content_table_body").append(item);
					/*$("#content_table_body").append("<hr>");*/
				});
			},
			complete:function(){
				hidePic();
			},
			error:function(){
				alert("Data Error!");
			}
		})
	}
	else{
		if(filename==''){
			alert("请输入文件名！");
		}else{
			$.ajax({
				url:"fileIndex",
				type:'get',
				data:{
					'root':root,
					'vague':vague,
					'type':type,
					'filename':filename,
					'filetype':searchType,
					'index':index
				},
				beforeSend:function(){
					showPic();
				},
				success:function(data){
					var ajson=eval("("+data+")");
					var Ist=ajson[0];
					var item;
					clearBox();
					if(Ist!=null||typeof(Ist)!="undefined"){
					$.each(ajson,function(key,value){
						item="<tr><td>"+value+"<tr><td>";//ajson对象的什么数据                       
						$("#content_table_body").append(item);
					});
				}
					else{
					alert("未查询到文件！");
				}
				}
				,
				complete:function(){
					hidePic();
				},
				error:function(){
					alert("Data Error!");
				}
			})
		}
	}
}
function clearBox(){
	$("#content-table tbody").html("");
}
function showPic(){
	$("#pic").show();
	$("#a").show();
	$("#all").hide();
}
function hidePic(){
	$("#pic").hide();
}
function hidePage(){
	$("#a").hide();
	$("#all").show();
}
function saveIndex(){
	var root=$("#root").val();
	var save=$("#save").val();
	$.ajax({
		url:"",
		data:{
			'root':root,
			'save':save
		},
		beforeSend:function(){
			showPic();
		},
		success:function(){
			alert("Save Indexes Successfully!");
		},
		complete:function(){
			hidePic();
		},
		error:function(){
			alert("Failed To Save!");
		}
		
	})
}
function p_mouseover(){
	$("#houtui").css('background','rgb(221, 218, 218)');
}
function p_mouseout(){
	$("#houtui").css('background','rgb(243,240,249)');
}