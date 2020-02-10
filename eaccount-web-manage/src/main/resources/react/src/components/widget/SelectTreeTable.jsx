const { Table } = antd;

class SelectTreeTable extends React.Component {
    genTreeDataMap(dataSource, treeDataMap) {
        if(!dataSource)
            return;
        dataSource.forEach(v => {
            treeDataMap.set(v.key, v);
            this.genTreeDataMap(v.children || [], treeDataMap);
        });
    }
    removeChildren(children, sets) {// 子节点取消选择
        const me = this;
        if(!children)
            return;
        children.forEach(v => {
            sets.delete(v.key);
            me.removeChildren(v.children, sets);
        })
    }
    getParent(pid, sets) {// 父节点选择
        const parent = this.treeDataMap.get(pid);
        if(!parent || (parent.pid && pid === parent.pid))
            return;
        sets.add(pid);
        this.getParent(parent.pid, sets);
    }
    render() {
        let { rowSelection: { selectedRowKeys } } = this.props;
        const { rowSelection: { onChange }, dataSource } = this.props;
        if(dataSource !== this.dataSource) {
            this.treeDataMap = new Map();
            this.dataSource = dataSource;
            this.genTreeDataMap(dataSource, this.treeDataMap);

        }
        return (
            <Table
                bordered
                {...this.props}
                rowSelection={{
                    selectedRowKeys: selectedRowKeys,
                    onChange: keys => {
                        selectedRowKeys = keys;
                        onChange(keys)
                    },
                    onSelect: (record, selected) => {
                        const set = new Set(selectedRowKeys);
                        if (selected === true || selected === 'true') {
                            this.getParent(record.pid, set);
                        } else {
                            this.removeChildren(record.children, set);
                        }
                        onChange(Array.from(set));
                    }
                }}
            />
        )
    }
}

module.exports = SelectTreeTable;