import sys;
import os

data_list = []

to_deploys = sys.argv[0]
lines = to_deploys.split('*')
for line in lines:
    str_array = line.split(':')
    oa = str_array[0]
    enable = str_array[1]

    data_list.append([oa, enable])

for data in data_list:
    oa = data[0]
    enable = data[1]

    print 'starting to set autostart, %s > enable is %s' % (oa, enable)

    deploy = AdminConfig.getid("/Deployment:%s-ear/" % oa)
    deployObj = AdminConfig.showAttribute(deploy, "deployedObject")
    targetMappings = AdminConfig.showAttribute(deployObj, "targetMappings")[1:-1]

    AdminConfig.modify(targetMappings, [['enable', enable]])

print 'saving...'
AdminConfig.save()

print 'done.'

