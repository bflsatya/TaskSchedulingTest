local result
local function deQueueFunction()
   local serviceName = tostring(KEYS[1])
   local circularListName = tostring(KEYS[2])
   local nextTenantId
   redis.call('MULTI')
   local isListExists = redis.call('exists',circularListName)
   local scoreAfterDecrement
   if isListExists == 1 then
      nextTenantId =  redis.call('lmove',circularListName, circularListName, 'LEFT', 'RIGHT')
      redis.call('ECHO','the value of nextTenantId is ' .. nextTenantId)
      local scoreAfterDecrement = redis.call('zincrby',serviceName, -1, nextTenantId)
      if ( tonumber(scoreAfterDecrement) <= 0 ) then
         redis.call('lrem', circularListName, 0, nextTenantId)
      end
      local tempString = '@class'
      local res = { [tempString] = "com.appviewx.controllers.LuaResponse", tenantId = tostring(nextTenantId), score = scoreAfterDecrement}
      result = cjson.encode(res)
--   else
--      result
   end
   redis.call('EXEC')
end

local function errorHandler(err)
--    result
end

xpcall(deQueueFunction, errorHandler)
return result