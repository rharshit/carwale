def filter_models(json, body_types=None):
    filter_dict = {}
    body_styles = []
    for model in json['modelList']:
        body_styles.append(model['bodyStyleName'])
    body_filter = []
    if body_types is not None:
        for model in json['modelList']:
            if model['bodyStyleName'] in body_types:
                body_filter.append(model)

    non_indian = []
    imported = []
    classic = []
    modified = []
    for model in json['modelList']:
        if not model['indian']:
            non_indian.append(model)
        if model['imported']:
            imported.append(model)
        if model['classic']:
            classic.append(model)
        if model['modified']:
            modified.append(model)

    filter_dict['body_filter'] = body_filter
    filter_dict['non_indian'] = non_indian
    filter_dict['imported'] = imported
    filter_dict['classic'] = classic
    filter_dict['modified'] = modified

    return filter_dict


def get_models(data, body_filter=False, non_indian=False, imported=False, classic=False, modified=False):
    models = []
    if body_filter:
        [models.append(x) for x in data['body_filter']]
    if non_indian:
        [models.append(x) for x in data['non_indian']]
    if imported:
        [models.append(x) for x in data['imported']]
    if classic:
        [models.append(x) for x in data['classic']]
    if modified:
        [models.append(x) for x in data['modified']]
    return [y for y in set([str(x['makeId']) + "." + str(x['rootId']) for x in models])]
