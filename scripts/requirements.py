import click
import os
import shutil
import uuid


def create_requirement(requirement_file, requirement_title, requirement_id, requirement_context):
    """
    Create a requirement file.

    :param: requirement_file: path and name of file (path does exist, file does not exist).
    :param: requirement_title: the title of the requirement.
    :param: requirement_id: the id of the requirement (system wide unique id)
    :param: requirement_context: the context/scope of the requirement.
    """
    template_file = os.path.join(os.getcwd(), "templates", "requirement.md")
    with open(template_file, 'r') as template_handle:
        content = template_handle.read()
        content = content.replace('${requirement.id.generate}', requirement_id)
        content = content.replace('${requirement.title}', requirement_title)
        content = content.replace('${requirement.context}', requirement_context)
        with open(requirement_file, 'w') as requirement_handle:
            requirement_handle.write(content)


def get_all_requirement():
    """ :return: list of all requirements. """
    requirements = []
    for root, folder, files in os.walk(os.path.join(os.getcwd(), 'docs', 'requirements')):
        for file in files:
            if file.startswith('req-') and file.endswith('.md'):
                requirements.append(get_requirement_details(os.path.join(root, file)))
    return requirements


def get_requirement_details(requirement_file):
    """
    Read title, id and context from requirement file.

    :param requirement_file: path and name of requirement file.
    :return: details of requirement file
    """
    details = {'title': '', 'id': '', 'context': ''}
    line_idx = 0
    with open(requirement_file, 'r') as handle:
        line = handle.readline()
        while line:
            if line_idx == 0:
                details['title'] = line.replace("# ", "").strip()
            elif line.find('**Id**:') > 0:
                details['id'] = line[line.find(':')+1:].strip()
            elif line.find('**Context**:') > 0:
                details['context'] = line[line.find(':')+1:].strip()
            line = handle.readline()
            line_idx += 1
    return details

def write_requirements_overview(requirements):
    """
    Writing the requirements.md as overview for all requirements.

    :param requirements: list of found requirements.
    """
    with open(os.path.join(os.getcwd(), 'docs', 'requirements.md'), 'w') as handle:
        handle.write('# Requirements\n')

        handle.write('| Id  | Title | Context | Details |\n')
        handle.write('| --- | ----- | ------- | ------- |\n')
        for requirement in requirements:
            details = '[details](%s)' % ('requirements/req-' + requirement['title'].lower() + '.md')
            handle.write(requirement['id'] + '|' + requirement['title'] + '|' +
                         requirement['context'] + '|' + details + '\n')


@click.group()
def main():
    pass

@main.command()
@click.option('--title', help="title of the requirement")
@click.option('--context', help="context of the requirement")
def create(**options):
    requirement_file = os.path.join(os.getcwd(), 'docs', 'requirements', "req-"+options['title'] + '.md').lower()
    requirement_path = os.path.split(requirement_file)[0]
    if not os.path.isdir(requirement_path):
        os.makedirs(requirement_path)
    if not os.path.isfile(requirement_file):
        requirements = get_all_requirement()

        if len(requirements) == 0:
            requirement_id = "1"
        else:
            requirement_id = str(max([int(entry['id']) for entry in requirements])+1)

        create_requirement(requirement_file, options['title'], requirement_id, options['context'])
        requirements.append({'title': options['title'], 'id': requirement_id, 'context': options['context']})
        write_requirements_overview(requirements)

@main.command()
def update(**options):
    requirements = get_all_requirement()
    write_requirements_overview(requirements)

if __name__ == "__main__":
    main()
