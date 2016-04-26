import re;

oas = ['b', 'c', 'e', 'f', 'h', 'i', 'j', 'k', 'l', 'n', 'p', 'q', 'u', 'v', 'w', 'y']

print 'starting...'
for oa in oas:
    deploy = AdminConfig.getid("/Deployment:qi%s-ear/" % oa)
    deployObj = AdminConfig.showAttribute(deploy, "deployedObject")
    targetMappings = AdminConfig.showAttribute(deployObj, "targetMappings")[1:-1]
    
    enable = AdminConfig.showAttribute(targetMappings, 'enable')

    str = 'qi%s=%s\n' % (oa, enable);
    print str