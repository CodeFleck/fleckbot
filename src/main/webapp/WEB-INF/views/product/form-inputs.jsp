<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
  	        <div class="form-group">
          <label for="name">name</label>
          <div class="input-group">
            <span class="input-group-addon"><i class="glyphicon glyphicon-unchecked"></i></span><form:input path='name' type='text'/>
<form:errors path='name'/>

          </div>
        </div>
        <div class="form-group">
          <label for="description">description</label>
          <div class="input-group">
            <span class="input-group-addon"><i class="glyphicon glyphicon-unchecked"></i></span><form:input path='description' type='text'/>
<form:errors path='description'/>

          </div>
        </div>
        <div class="form-group">
          <label for="price">price</label>
          <div class="input-group">
            <span class="input-group-addon"><i class="glyphicon glyphicon-unchecked"></i></span><form:input path='price' type='text'/>
<form:errors path='price'/>

          </div>
        </div>
        <div class="form-group">
          <label for="category.ID">category.ID</label>
          <div class="input-group">
            <span class="input-group-addon"><i class="glyphicon glyphicon-unchecked"></i></span><form:select path='category.ID' items='${categoryList}' itemValue='ID' itemLabel='name'>
</form:select>
<form:errors path='category.ID'/>

          </div>
        </div>
