local overseer = require('overseer')

local function gradle(task, args)
  return function()
    return {
      name = 'gradle ' .. task,
      cmd = 'sh gradlew ' .. task .. ' --daemon ' .. (args or ''),
      components = {
        { 'on_complete_dispose', timeout = 10, require_view = { 'FAILURE', }, },
        'default',
      },
    }
  end
end

overseer.register_template({
  name = 'daemon',
  builder = gradle('scan', '--foreground'),
})

overseer.run_template({ name = 'daemon', })

overseer.register_template({
  name = 'build',
  builder = gradle('build'),
})

overseer.register_template({
  name = 'clean',
  builder = gradle('clean'),
})

overseer.register_template({
  name = 'minecraft',
  builder = gradle('runClient'),
})

overseer.register_template({
  name = 'spigot',
  builder = function()
    return {
      name = 'spigot',
      cmd = 'java -jar spigot-1.21.1.jar --nogui',
      cwd = 'run',
      components = {
        'default',
        'unique',
      },
    }
  end
})

vim.cmd([[
augroup gradlew
  autocmd!
  autocmd VimLeave * !./gradlew --stop
augroup END
]])
