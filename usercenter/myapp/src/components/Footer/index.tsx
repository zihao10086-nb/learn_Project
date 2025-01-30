import { GithubOutlined } from '@ant-design/icons';
import { DefaultFooter } from '@ant-design/pro-components';
import {GITHUB_LINK} from "@/constants";
const Footer: React.FC = () => {
  const defaultMessage = '翘楚宇宙';
  const currentYear = new Date().getFullYear();
  return (
    <DefaultFooter
      copyright={`${currentYear} ${defaultMessage}`}
      links={[
        {
          key: 'study project',
          title: '学习项目',
          href: GITHUB_LINK,
          blankTarget: true,
        },
        {
          key: 'github',
          title: <><GithubOutlined /> 翘楚</>,
          href: GITHUB_LINK,
          blankTarget: true,
        },
        {
          key: 'qiaochu universe',
          title: '翘楚宇宙',
          href: GITHUB_LINK,
          blankTarget: true,
        },
      ]}
    />
  );
};
export default Footer;
