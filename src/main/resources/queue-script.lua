local result = nil
local function queueFunction()
    local serviceName = tostring(KEYS[1])
    local tenantId = ARGV[1]
    local circularListName = tostring(KEYS[2])
    local scoreAfterIncrement = redis.call('zadd',serviceName, 'INCR', 1, tenantId)
    local tenantIndex = redis.call('lpos',circularListName, tenantId)
    redis.call('ECHO','the value of tenantId is ' .. tenantId)
--    redis.call('ECHO','the value of tenantIndex is ' .. tenantIndex)
    if tenantIndex == false then
        redis.call('rpush',circularListName, tenantId)
    end
    local tempString = '@class'
    local res = {[tempString] = "com.appviewx.controllers.LuaResponse", tenantId = tenantId, score = scoreAfterIncrement}
    result = cjson.encode(res)
end

local function errorHandler(err)
    result = nil
end

xpcall(queueFunction, errorHandler)
return result