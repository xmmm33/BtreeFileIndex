<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
    <link rel="stylesheet" href="css/show.css">
    <script type="text/javascript" src="js/jquery-3.2.1.min.js"></script> 
    <script src="js/mydata3.js"></script>
</head>
<body>
    <div id="all">
        <div >
            <div id="head">
                <span>File&nbsp;</span>
                <img src="timg.png" alt="图片显示有误！"width="100px" height="50px">
                <span> Index</span>
            </div>
        </div>
        <form>
            <select id="root"></select>
            <input type="text" id="filename" placeholder="Please  input  the  filename !" autocomplete="off">
            <img class="tubiao" src="sousuo4.png" alt="图片显示有误！" width="20px" height="20px">
            <input class="anniu" value="Search" style="color:white;font-weight:700" onclick="showContent()">
       </form>
      
        <div id="sort">
            <table class="type_table" >
                <tr>
                    <th>Search:</th>
                    <td ><input id="vague" type="radio" name="searchsort" value="Vague" onclick="searchSort(this)">Vague Search</td>
                    <td><input id="type" type="radio" name="searchsort" value="Type" onclick="searchSort(this)">Type Search</td>
                    <td><input id="rebuild" type="radio" name="searchsort1" value="Rebuild" onclick="searchSort(this)">Rebuild</td>
                </tr>
                <tr>
                    <th>Media:</th>
                    <td >
                       &nbsp;&nbsp;&nbsp;<img src="music.jpg" alt="图片显示有误！">
                        &nbsp;&nbsp;&nbsp;<input type="button" value="音乐" name="searchtype" style="width:55px;height:28px">
                    </td>
                    <td>
                        &nbsp;&nbsp;&nbsp;<img src="image.jpg" alt="图片显示有误！">
                        &nbsp;&nbsp;&nbsp;<input type="button" value="图片" name="searchtype"style="width:55px;height:28px">
                    </td>
                    <td>
                         &nbsp;&nbsp;&nbsp;<img src="movie.jpg" alt="图片显示有误！">
                        &nbsp;&nbsp;&nbsp;<input type="button" value="影视" name="searchtype"style="width:55px;height:28px">
                    </td>
                    <td>
                        
                        &nbsp;&nbsp;&nbsp;<img src="doc.jpg" alt="图片显示有误！">
                        &nbsp;&nbsp;&nbsp;<input type="button" value="文档" name="searchtype"style="width:55px;height:28px">
                    </td>
                </tr>
            </table>
            <div id="footer">
                <button onclick="saveIndex()" id="save"><img src="save.png" alt=""> Save</button>
                <button onclick="reset()" id="reset" type="reset"><img src="qingkong.png" alt=""> Reset</button>
            </div>
        </div>    
    </div>
    <div id="a">
        <div>
            <p onclick="hidePage()" id="houtui" onmouseover="p_mouseover()" onmouseout="p_mouseout()"><img class="Img" src="houtui.png" alt=""></p>
            <div class="head">
                <span>File&nbsp;</span>
                <img src="timg.png" alt="图片显示有误！"width="100px" height="50px">
                <span> Index</span>
            </div>
        </div>
        <div id="showContent">
            <table id="content-table">
                <tbody id="content_table_body">  
                    <!-- 显示动态数据 -->
                </tbody>
            </table>
        </div>
    </div>
    <div id="pic" style="display:none">
            <img src="quan.gif" alt="">
    </div>
</body>
</html>