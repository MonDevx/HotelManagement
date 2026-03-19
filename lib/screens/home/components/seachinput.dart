import 'package:flutter/material.dart';
import 'package:flutter_typeahead/flutter_typeahead.dart';
import 'package:mini_project/helpers/helperdictionary.dart';
import 'package:mini_project/localization/localizations.dart';

class SearchInput extends StatefulWidget {
  final Function callSetStateWordTh;
  final Function callSetStateWordEng;
  SearchInput(this.callSetStateWordTh, this.callSetStateWordEng);

  @override
  _SearchInputState createState() => _SearchInputState();
}

class _SearchInputState extends State<SearchInput> {
  final TextEditingController _controller = TextEditingController();

  @override
  void initState() {
    super.initState();
    _controller.addListener(_onTextChanged);
  }

  void _onTextChanged() {
    setState(() {});
  }

  @override
  void dispose() {
    _controller.removeListener(_onTextChanged);
    _controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final labels = AppLocalizations.of(context);
    final String pattern = r'^[a-z A-Z,.\-]+$';
    final RegExp regExp = RegExp(pattern);
    String checkthoreng = "";
    return TypeAheadField(
      textFieldConfiguration: TextFieldConfiguration(
        controller: _controller,
        decoration: InputDecoration(
          prefixIcon: const Icon(Icons.search),
          suffixIcon: _controller.text.isEmpty
              ? null
              : IconButton(
                  icon: const Icon(Icons.clear),
                  onPressed: () {
                    _controller.clear();
                    FocusScope.of(context).unfocus();
                  },
                ),
          border: OutlineInputBorder(
            borderRadius: BorderRadius.circular(25.0),
          ),
          hintText: labels?.seacrh?.seachInputhintlabel,
        ),
      ),
      suggestionsCallback: (query) async {
        if (query.trim().isEmpty) return [];
        checkthoreng = query;
        return !regExp.hasMatch(query)
            ? await HelperDictionary.getAllWordsLikeTh(query.toLowerCase())
            : await HelperDictionary.getAllWordsLikeEn(query.toLowerCase());
      },
      itemBuilder: (context, words) {
        return ListTile(
          leading: const Icon(Icons.translate),
          title: Text(!regExp.hasMatch(checkthoreng)
              ? words.getTsearch
              : words.getEsearch),
        );
      },
      onSuggestionSelected: (words) {
        !regExp.hasMatch(checkthoreng)
            ? widget.callSetStateWordTh(words)
            : widget.callSetStateWordEng(words);
      },
    );
  }
}
