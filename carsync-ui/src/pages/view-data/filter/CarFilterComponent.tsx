import { Flex, TreeSelect } from "antd";
import { DataNode } from "antd/es/tree";
import { MakeModels } from "./FilterComponent";

const { SHOW_CHILD } = TreeSelect;

type CarFilterProps = {
    allMakeModels: MakeModels[],
    selectedCars: string[],
    setSelectedCars: React.Dispatch<React.SetStateAction<string[]>>
}

const dataNodeComparator = ((a: DataNode, b: DataNode) => (a.title ?? '').toString().localeCompare((b.title ?? '').toString()))

export function CarFilterComponent(carFilterProps: CarFilterProps) {
    const { allMakeModels, selectedCars, setSelectedCars } = carFilterProps;

    const onChange = (newValue: string[]) => {
        setSelectedCars(newValue);
    };

    function getTreeData(): import("rc-tree-select/lib/interface").DataNode[] | undefined {
        return allMakeModels.map(make => {
            const models = make.models
            const makeNode = {
                title: make.make,
                value: JSON.stringify([make.make]),
                key: JSON.stringify([make.make]),
                children: models.map(model => {
                    const variants = model.variants
                    const modelNode = {
                        title: model.name,
                        value: JSON.stringify([make.make, model.name]),
                        key: JSON.stringify([make.make, model.name]),
                        children: variants.map(variant => {
                            const variantNode = {
                                title: model.name + ' ' + variant.name,
                                value: JSON.stringify([make.make, model.name, variant.name]),
                                key: JSON.stringify([make.make, model.name, variant.name]),
                            }
                            return variantNode as DataNode;
                        }).sort(dataNodeComparator)
                    }
                    return modelNode as DataNode;
                }).sort(dataNodeComparator)
            }
            return makeNode as DataNode;
        }).sort(dataNodeComparator)
    }

    const tProps = {
        onChange,
        treeCheckable: true,
        showCheckedStrategy: SHOW_CHILD,
        placeholder: 'Please select',
        style: {
            width: '100%',
        },
    };


    return <>
        <Flex style={{ maxHeight: 200, width: 690 }}>
            <TreeSelect
                treeData={getTreeData()}
                value={selectedCars}
                {...tProps}
                placeholder="Select cars"
                allowClear
                autoClearSearchValue={false}
                style={{ maxHeight: 200, maxWidth: 690, minWidth: 420 }}
                maxTagCount={20}
                maxTagTextLength={20}
            />
        </Flex>
    </>;
}
