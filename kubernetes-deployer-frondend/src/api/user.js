import fetch from '@/utils/fetch'

export function getList(query) {
  return fetch({
    url: '/home/users',
    method: 'get',
    params: query
  })
}
