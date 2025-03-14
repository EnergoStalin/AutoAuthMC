vim.fn.setenv('PATH', '/usr/lib/jvm/java-21-openjdk/bin:' .. vim.fn.getenv('PATH'))

local overseer = require('overseer')

local function gradle(task, args, config)
  config = config or {}
  config.name = config.name or ('gradle ' .. task)

  return function()
    return {
      name = config.name,
      cmd = 'sh gradlew ' .. task .. ' --daemon ' .. (args or ''),
      components = {
        'default',
        { 'on_complete_dispose', timeout = 10, require_view = { 'FAILURE', }, },
        config.components and unpack(config.components) or nil,
      },
    }
  end
end

overseer.register_template({
  name = 'daemon',
  builder = gradle('scan', '--foreground', { name = 'daemon', components = { 'unique', }, }),
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
  name = 'dependencies',
  builder = gradle('--refresh-dependencies'),
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
