<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="js/jquery-3.2.1.min.js"></script> 
<script type="text/javascript" src="js/mydata2.js"></script>
<link type="text/css" rel="stylesheet" href="css/show.css">

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IndexFile</title>
</head>
<body>
<center>
<h1>B+树文件索引</h1>
</center>
 <div id="mydiv"> 
 <!-- 输入框 -->
 <center>
 <form >
 请输入待搜索文件名：<input type="text"  size="50" id="filename" /><br>
  请输入待搜索磁盘：<select id="root"></select>
<br/>
是否进行模糊查询：  <input type='radio' name='vague' id='v0' onclick="rule()" value='Yes' >是
<input type='radio' name='vague' onclick="rule()"id='v1' value='No' >否<br>
是否进行类型查询：  <input type='radio' onclick="rule()" id='t0' name='type'  value='Yes' >是
<input type='radio' name='type'  onclick="rule()" id='t1' value='No' >否<br>
是否进行重建索引：           <input type='radio' name='index'  id = 'i0' value='Yes' >是
<input type='radio' name='index'  id='i1' value='No' >否<br>
<input type='button' onclick="getMoreContents()" value='搜索'>
<input type="button" value="清空" onClick="reset()" type="reset">
<input type="button" value="保存索引" onclick="saveindex()"  id="save" value="save"> <br>
<input type="button" name="searchtype" value="影视" id='movie' onclick="getMoreContents()" style="border-style: solid; border-width: 0; ">
<input type="button" name="searchtype" value="音乐" id='music' onclick="getMoreContents()" style="border-style: solid; border-width: 0">
<input type="button" name="searchtype" value="图片" id='img' onclick="getMoreContents()" style="border-style: solid; border-width: 0">
<input type="button" name="searchtype" value="文档" id='doc'onclick="getMoreContents()"  style="border-style: solid; border-width: 0">
<div id="loading" style="display: none;">
	<img alt="" src="img/search.gif" >
</div>
<div id="overlay" class="overlay"></div>    

 </form>
 </center>
 <!-- 下面是内容展示区域 -->
 <div id="popDiv"> 
  <table id="content-table" bgcolor="#FFFAFA" border="0" cellspacing="0" cellpadding="0"> 
   <tbody id="content_table_body"> 
    <!-- 动态查询出来的数据显示在这里 -->
   </tbody> 
  </table> 
 </div> 
 </div>
 <script type="text/javascript">
 
 </script>
 </body>
</html>