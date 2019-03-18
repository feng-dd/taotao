<%--
  Created by IntelliJ IDEA.
  User: 冯大大
  Date: 2018/8/31
  Time: 11:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div>
    <a class="easyui-linkbutton" onclick="importIndex()">导入数据到索引库</a>
</div>
<script type="text/javascript">
    function importIndex(){
        $.post("/index/import",null,function(data){
            if(data.status == 200){
                $.messager.alert('提示','导入数据到索引库成功!');
            }else{
                $.messager.alert('提示','导入数据到索引库失败!');
            }
        });
    }
</script>

