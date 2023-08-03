import request from '@/utils/request'

export default{
  getUserList(searchModel){
    return request({
      url: '/user/list',
      method: 'get',
      params:{
        pageNo:searchModel.pageNo,
        pageSize:searchModel.pageSize,
        username:searchModel.username,
        phone :searchModel.phone
      }
    })
  },
  addUser(User){
    return request({
      url: '/user',
      method: 'post',
      data: User
    })
  },
  saveUser(user){
   if(user.id==null && user.id==undefined)
   {
     return this.addUser(user);
   }else{
    return this.updateUser(user);
   }
  },
  updateUser(User){
    return request({
      url: '/user',
      method: 'put',
      data: User
    })
  },
  getUserById(id){
    return request({
      url:`/user/${id}`,
      method: 'get'
    })
  },
  deleteUserById(id){
    return request({
      url:`/user/${id}`,
      method: 'delete'
    })
  }
}
