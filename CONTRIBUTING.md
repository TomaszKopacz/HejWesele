# Contributing

[[_TOC_]]

## Branches

`main` branch contains current project code. All the changes you want to introduce to the project should be merged to that branch.

Branches that are to be merged to the `main` should be named in a form of `<prefix>/<jira-issue-id>-simplified-task-name`,
e.g. `feature/XYZ-123-add-contributing-docs` or `fix/XYZ-124-app-name-typo`.

Sometimes there's a need to introduce a larger number of changes to the feature branch and merge it to `main` after a longer time - 
for example, if you're working on a larger story that would break `main` branch if merging tasks one-by-one. In such case, you can 
introduce personal branches (`<username>/<jira-issue-id>-simplified-task-name`) and merge them to the feature branch over a time. 
Do not create merge commits when merging to the feature branch. When the feature branch is ready, merge it to the `main` branch.
Create merge commit during merge to `main` to allow to inspect whole feature code at once, while preserving commits history. 
Another option is to squash all commits (if you don't need to keep commits history), and then you don't need to create merge commit.

## Rebase instead of merge

If you're working on your feature branch, and you need to sync it with `main`, do not merge `main` into the branch. This will 
unnecessarily create merge commit and spoil your branch commits. Instead, rebase your branch with `main`. Same applies when working 
with feature branch and personal branches.

## Commits

Commits should be named in a simple form of:
```
<jira-issue-id> Description
Optional additional description
```

For example:
```
XYZ-123 Add CONTRIBUTING.md
This adds docs with guidelines that can be helpful for new contributors.
```
```
XYZ-124 Fix app name typo
```

## Merge requests

When creating merge request, first make sure your branch is rebased with `main`. You can also run the same Gradle command that is 
invoked on the CI before push to see if MR checks won't immediately fail on CI.

Make sure to assign reviewer when creating merge request.

After code review, there may be some changes that should be introduced before approve and merge. Suggested way to handle introducing
changes after code review, especially if your MR contains more than one commit, is to create fixups and then use autosquash to squash 
them into original commits. This way you can preserve original commits structure while adding necessary fixes. 
See [this article](https://thoughtbot.com/blog/autosquashing-git-commits) to learn how to work with this flow.

## Documentation

All the code that can be relevant to the person who will be using this project should be documented.
Add documentation together with the new code, in the same MR as your changes - do not postpone adding/updating documentation.
