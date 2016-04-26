import sys;
import os;
import time;

to_deploy = sys.argv[1].split(':')
workspace_path = sys.argv[0]

now = lambda: int(round(time.time()))
time_restriction = 120 #sec

def check_last_modified_time(filepath):
    is_valid = 1 == 0;
    exist = os.path.isfile(filepath)
    if exist:
        last_modified_time = os.path.getmtime(filepath)

        if (now() - last_modified_time) < time_restriction:
            is_valid = 1 == 1

    return is_valid

print 'msg.deploy.preparing,%s' % to_deploy


is_all_deployed = 0
deployed_count = 0
to_deploy_list = []

for oa in to_deploy:
    to_deploy_dict = {};#dict = dictionary(python) = map(java)
    
    app_name = 'qi%s-ear' % oa
    to_deploy_dict['app_name'] = app_name
    ear_path = '%s/qi%s/qi%s-ear/target/qi%s-ear.ear' % (workspace_path, oa, oa, oa)
    to_deploy_dict['ear_path'] = ear_path
    to_deploy_dict['is_deployed'] = 0
    print ear_path

    to_deploy_list.append(to_deploy_dict)


while not is_all_deployed:
    for dict in to_deploy_list:
        if not dict['is_deployed']:
            if not check_last_modified_time(dict['ear_path']):
                print 'msg.deploy.oa.waiting,%s' % dict['app_name']
                time.sleep(2)
            else:
                print 'msg.deploy.oa.starting,%s' % dict['app_name']
                AdminApp.update(dict['app_name'], 'app', '[-operation update -contents %s]' % dict['ear_path'])

                dict['is_deployed'] = 1
                deployed_count += 1

    is_all_deployed = (deployed_count == len(to_deploy_list))



print 'msg.deploy.saving'
AdminConfig.save()
