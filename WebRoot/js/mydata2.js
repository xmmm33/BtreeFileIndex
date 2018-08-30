//原版js
$(function(){
	        $("#i1").prop("checked",true)
            var btns = $("input[name='searchtype']");
            for (var i=0;i<btns.length;i++){
                btns[i].onclick = function(){
                	getMoreContents(this.value);
                };
            }
        })
 //1.获得用户输入内容的关联信息的函数 
 function reset(){
	$(" input[ type='text' ] ").val('');
	$(" input[ type='radio' ] ").val('');
}
function rule(){
	if($("input[name='vague']:checked").val()=="Yes"){
		$("#t1").prop("checked",true)
	}
	if($("input[name='type']:checked").val()=="Yes"){
		$("#v1").prop("checked",true)
	}
}
function saveindex(){ 
	 //首先获得用户输入 
	 var content = $("#filename").val(); 
	 var index = $("input[name='index']:checked").val();
	 var vague = $("input[name='vague']:checked").val();
	 var root = $("#root").val();
	 var type = $("input[name='type']:checked").val();
	 var save = $("#save").val;
		 $.ajax({
			empty:true,
			 async:true,
			beforeSend:function(){
				showPic();
			},
			complete:function(){
				hiddenPic();
			},
			url:"fileIndex",
			type:'get',
			data:"save="+save+"&root="+root,
			success:function(data){
				var json = eval("("+data+")"); 
				 var con=json[0];
				 if(con =="OK"){
					 alert("保存成功！！");
				 }
			}
		 });
	}
   

$.ajax({ 
	url : "diskname", 
	type : "post", 
	success: function(data){ 
	var jsonObj=eval("("+data+")"); 
	$.each(jsonObj, function (i, item) { 
	jQuery("#root").append("<option value="+ item+">"+ item+"</option>"); 
	}); 
	jQuery("#root").append("<option value="+'全盘符索引'+">"+'全盘符索引'+"</option>");    
	}, 
	error: function(text) {} 
	}); 

 function getMoreContents(filetype){ 
 //首先获得用户输入 
	 var content = $("#filename").val(); 
	 var index = $("input[name='index']:checked").val();
	 var vague = $("input[name='vague']:checked").val();
	 var root = $("#root").val();
	 var type = $("input[name='type']:checked").val();
	 var save = $("#save").val;
	 showOverlay();
	 if(content.value == ""){ 
		  //当输入框为空时，清空之前的数据 
		  clearContent(); 
		  return; 
} 
	 if(typeof(filetype)=="undefined"){
		 filetype='';
	 }
	 else{
		 content='';
	 }
	 
	 $.ajax({
		empty:true,
		 async:true,
		beforeSend:function(){
			showPic();
		},
		complete:function(){
			hideOverlay();
			hiddenPic();
		},
		url:"fileIndex",
		type:'get',
		data:"filename="+content+"&index="+index+"&root="+root+"&vague="+vague+"&type="+type+"&filetype="+filetype,
		success:function(data){
			//解析获得的数据 
			 var json = eval("("+data+")"); 
			 //获得这些数据之后，就可以动态的显示数据了。把这些数据展示到输入框下面。 
			 setContent(json); 
		}
	 });
}
 function showOverlay() {
	 alert('ddd');
    $("#overlay").height(pageHeight());
    $("#overlay").width(pageWidth());

    // fadeTo第一个参数为速度，第二个为透明度
    // 多重方式控制透明度，保证兼容性，但也带来修改麻烦的问题
    $("#overlay").fadeTo(200, 0.5);
}

 隐藏覆盖层 
function hideOverlay() {
    $("#overlay").fadeOut(200);
}

 当前页面高度 
function pageHeight() {
    return document.body.scrollHeight;
}

 当前页面宽度 
function pageWidth() {
    return document.body.scrollWidth;
}
 
 //显示加载数据
 function showPic(){
	 $("#loading").show();
	 clearContent();
 }
 
 
 //隐藏加载数据
 function hiddenPic(){
	 $("#loading").hide();
 }
 
 //设置关联数据的展示，参数代表服务器传递过来的关联数据 
 function setContent(contents){ 
  //设置位置 
	 setLocaltion(); 
  //首先获得关联数据的长度，以此来确定生成多少个<tr></tr> 
	 var size = contents.length; 
	 var con=contents[0];
     if(con==null&&typeof(con)!="undefined"){
    	 alert('未找到您要搜索的文件！');
     }else if(size==0){
    	 alert('请输入您要搜索的文件名！');
     }else
     {
    	 
     
  //设置内容 
	 for(var i =0;i < size;i++){ 
		 var nextNode = contents[i];//代表json数据的第i个元素 
		 var tr = document.createElement("tr"); 
		 var td = document.createElement("td"); 
		 td.setAttribute("borde","0"); 
		 td.setAttribute("gbcolor","#FFFAFA"); 
		
   //为td绑定两个样式（鼠标进入和鼠标移出时事件） 
		 td.onmouseover = function(){ 
			 this.className = 'mouseOver'; 
		 }; 
		 td.onmouseout = function(){ 
			 this.className = 'mouseOut'; 
		 }; 
     
     
   //创建一个文本节点 
   	var text = document.createTextNode(nextNode); 
   	td.appendChild(text);
   	
   	tr.appendChild(td); 
   	$("#content_table_body").append(tr); 
	 }
     }
 } 
 
 //清空之前的数据 
 function clearContent(){ 
	 $("#content_table_body").empty();
  
}
 
 //设置显示关联信息的位置 
 function setLocaltion(){ 
  //关联信息的显示位置要和输入框一致 
	  var content = $("#filename"); 
	  var width = content.offsetWidth;//输入框的长度 
	  var left = content["offsetLeft"];//到左边框的距离 
	  var top = content["offsetTop"]+content.offsetHeight;//到顶部的距离(加上输入框本身的高度) 
  //获得显示数据的div 
	  var popDiv = $("#popDiv"); 
	  popDiv.css({'border':"gray 1px solid",'left':left+'px','top':top+'px','width':width+'px'});
	  
	  $("#content-table").css('width',width+'px'); 
 } 